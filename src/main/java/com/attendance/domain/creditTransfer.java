package com.attendance.domain;

public class creditTransfer {
    private Integer id;

    private Integer workingHours;

    private Integer learnHours;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Integer workingHours) {
        this.workingHours = workingHours;
    }

    public Integer getLearnHours() {
        return learnHours;
    }

    public void setLearnHours(Integer learnHours) {
        this.learnHours = learnHours;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", workingHours=").append(workingHours);
        sb.append(", learnHours=").append(learnHours);
        sb.append("]");
        return sb.toString();
    }
}