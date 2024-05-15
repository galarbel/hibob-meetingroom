public class MeetingRoom implements Comparable<MeetingRoom> {
    public int capacity;
    public String roomName; // assuming room name is unique

    public MeetingRoom(int capacity, String roomName) {
        this.capacity = capacity;
        this.roomName = roomName;
    }

    @Override
    public int compareTo(MeetingRoom other) {
        return Integer.compare(this.capacity, other.capacity);
    }

    @Override
    public String toString() {
        return roomName + " (" + capacity + ")";
    }
}
