import java.time.LocalDate;
import java.util.*;

public class MeetingRoomFinder {

    public Map<String, MeetingRoomResponse> bookings;
    public List<MeetingRoom> sortedRoomListBySize;


    public MeetingRoomFinder(List<MeetingRoom> roomList) {
        this.sortedRoomListBySize = new ArrayList<>(roomList);
        this.sortedRoomListBySize.sort(null);
        this.bookings = new Hashtable<>();
    }

    private String _getBookingKey(MeetingRoom room, LocalDate localDate) {
        return room.roomName + "_" + localDate.toString();
    }

    private boolean _checkIsRoomBookedByDate(MeetingRoom room, LocalDate localDate) {
        return this.bookings.containsKey(_getBookingKey(room, localDate));
    }

    private List<MeetingRoom> _getRelevantRoomsBySize(int requiredCapacity) {
        // simple binary search that searches for the minimum relevant option
        int low = 0;
        int high = this.sortedRoomListBySize.size() - 1;
        int resultIndex = -1;

        // edge case
        if (requiredCapacity > this.sortedRoomListBySize.get(high).capacity) {
            return Collections.emptyList();
        }

        // yay for Chat-GPT.
        while (low <= high) {
            int mid = low + (high - low) / 2;
            MeetingRoom room = this.sortedRoomListBySize.get(mid);
            if (room.capacity >= requiredCapacity) {
                resultIndex = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (resultIndex != -1) {
            return this.sortedRoomListBySize.subList(resultIndex, this.sortedRoomListBySize.size());
        }

        return Collections.emptyList();
    }

    public MeetingRoomResponse bookMeetingRoom(MeetingRoomRequest roomRequest) {
        List<MeetingRoom> meetingRoomOptions = _getRelevantRoomsBySize(roomRequest.numParticipants);
        LocalDate requiredDate = roomRequest.meetingDate;

        for (MeetingRoom roomOption: meetingRoomOptions) {
            if (!_checkIsRoomBookedByDate(roomOption, requiredDate)) {
                MeetingRoomResponse response = new MeetingRoomResponse(roomRequest, roomOption);
                this.bookings.put(_getBookingKey(roomOption, requiredDate), response);
                return response;
            }
        }
        return null;
    }

    public void cancelRequest(MeetingRoomResponse roomResponse) {
        // as we talked in the interview - assuming this passes integrity checks (permissions + request exists and not already cancelled, etc'.)
        // _checkCancelRequestValid() ...
        this.bookings.remove(_getBookingKey(roomResponse.meetingRoom, roomResponse.meetingRoomRequest.meetingDate));
    }


}

class TestMeetingRoomFinder {

    public static void print(Object o) { // debugger/test helper :)
        System.out.println(o);
    }

    public static void main(String[] args) {
        List<MeetingRoom> meetingRooms = new ArrayList<>();
        meetingRooms.add(new MeetingRoom(4, "small room"));
        meetingRooms.add(new MeetingRoom(12, "xl room"));
        meetingRooms.add(new MeetingRoom(8, "md room"));
        meetingRooms.add(new MeetingRoom(8, "md room 2"));

        MeetingRoomFinder roomFinder = new MeetingRoomFinder(meetingRooms);
        MeetingRoomResponse res1 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(14, "Too big", LocalDate.now()));
        assert (res1 == null);

        MeetingRoomResponse res2 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(10, "xl req", LocalDate.now()));
        print(res2.meetingRoom);

        MeetingRoomResponse res3 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(10, "xl req", LocalDate.now()));
        print(res3);

        roomFinder.cancelRequest(res2);
        MeetingRoomResponse res4 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(10, "xl req", LocalDate.now()));
        print(res4.meetingRoom);

        MeetingRoomResponse res5 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(10, "xl req", LocalDate.of(2024,1, 1)));
        print(res5.meetingRoom);

        MeetingRoomResponse res6 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(2, "small req 1", LocalDate.now()));
        MeetingRoomResponse res7 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(2, "small req 2", LocalDate.now()));
        MeetingRoomResponse res8 = roomFinder.bookMeetingRoom(new MeetingRoomRequest(2, "small req 3", LocalDate.now()));
        print(res6.meetingRoom);
        print(res7.meetingRoom);
        print(res8.meetingRoom);
    }


}