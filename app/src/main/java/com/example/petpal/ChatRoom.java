package com.example.petpal;

public class ChatRoom {
    private String roomId;


    // Constructor, getters y setters

    public ChatRoom(String roomId) {

        this.roomId = roomId;

    }

    public ChatRoom() {

    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


}
