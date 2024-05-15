import java.time.LocalDate;

public class MeetingRoomRequest {
    public int numParticipants;
    public String ownerName;
    public LocalDate meetingDate;

    public MeetingRoomRequest(int numParticipants, String ownerName, LocalDate meetingDate) {
        this.numParticipants = numParticipants;
        this.ownerName = ownerName;
        this.meetingDate = meetingDate;
    }
}
