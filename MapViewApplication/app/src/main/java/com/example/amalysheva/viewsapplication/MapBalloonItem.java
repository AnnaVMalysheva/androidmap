package com.example.amalysheva.viewsapplication;

public class MapBalloonItem {
    private Integer id;
    private Integer balloon_id;
    private String address;

    public MapBalloonItem(Integer balloon_id, Integer id, String address) {
        this.id = id;
        this.balloon_id = balloon_id;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBalloon_id() {
        return balloon_id;
    }

    public void setBalloon_id(Integer balloon_id) {
        this.balloon_id = balloon_id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapBalloonItem that = (MapBalloonItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (balloon_id != null ? !balloon_id.equals(that.balloon_id) : that.balloon_id != null)
            return false;
        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (balloon_id != null ? balloon_id.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
