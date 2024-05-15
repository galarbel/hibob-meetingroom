
public class MeetingRoomResponse {
    public MeetingRoomRequest meetingRoomRequest;
    public MeetingRoom meetingRoom;

    public MeetingRoomResponse(MeetingRoomRequest meetingRoomRequest, MeetingRoom meetingRoom) {
        this.meetingRoomRequest = meetingRoomRequest;
        this.meetingRoom = meetingRoom;
    }
}

