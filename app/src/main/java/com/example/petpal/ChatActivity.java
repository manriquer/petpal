package com.example.petpal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseUser currentUser;
    private DatabaseReference chatRef;
    private TextView messageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListView listView = findViewById(R.id.list_view);
        EditText input = findViewById(R.id.input);
        String chatName = getIntent().getStringExtra("chatName");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();
        chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatName);
        displayChatMessages(listView);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = input.getText().toString();
                if (!messageText.isEmpty()) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userName = snapshot.child("username").getValue(String.class);
                                if (userName != null && !userName.isEmpty()) {
                                    chatRef.push().setValue(new ChatMessage(messageText, userName));
                                } else {
                                    chatRef.push().setValue(new ChatMessage(messageText, "Usuario"));
                                }
                            } else {
                                chatRef.push().setValue(new ChatMessage(messageText, "Anónimo"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            chatRef.push().setValue(new ChatMessage(messageText, "Usuario_Anónimo"));
                        }
                    });
                    input.setText("");
                }
            }
        });
    }

    private void displayChatMessages(ListView listView) {
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.message)
                .setQuery(chatRef, ChatMessage.class)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                long timeInMillis = model.getMessageTime();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String formattedTime = sdf.format(new Date(timeInMillis));

                messageTime.setText(formattedTime);
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
