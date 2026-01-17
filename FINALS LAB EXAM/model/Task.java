package model;

public class Task extends AbstractTask {

    public String status = "NOT STARTED";

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;

    }

}
