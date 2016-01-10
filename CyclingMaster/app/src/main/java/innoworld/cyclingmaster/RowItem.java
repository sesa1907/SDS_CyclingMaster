package innoworld.cyclingmaster;

/**
 * Created by sesar on 05/01/2016.
 */
public class RowItem {
    private String rank;
    private String member_name;
    private int profile_pic_id;
    private String totalride;
    private int badge;
    private String xp;

    public RowItem(String rank, String member_name, int profile_pic_id, String totalride,
                   int badge, String xp) {

        this.rank = rank;
        this.member_name = member_name;
        this.profile_pic_id = profile_pic_id;
        this.totalride = totalride;
        this.badge = badge;
        this.xp = xp;
    }
    public String getRank() { return rank;}

    public void setRank(String rank) { this.rank = rank;}

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public int getProfile_pic_id() {
        return profile_pic_id;
    }

    public void setProfile_pic_id(int profile_pic_id) {
        this.profile_pic_id = profile_pic_id;
    }

    public String gettotalride() {
        return totalride;
    }

    public void settotalride(String totalride) {
        this.totalride = totalride;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getxp() {
        return xp;
    }

    public void setxp(String xp) {
        this.xp = xp;
    }
}
