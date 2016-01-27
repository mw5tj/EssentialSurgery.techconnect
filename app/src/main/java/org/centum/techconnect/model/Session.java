package org.centum.techconnect.model;

/**
 * Created by Phani on 1/26/2016.
 */
public class Session {

    private long createdDate;
    private String department;
    private Device device;
    private Urgency urgency;
    private DeviceProblem deviceProblem;
    private String notes;
    private Flowchart currentFlowchart;

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public DeviceProblem getDeviceProblem() {
        return deviceProblem;
    }

    public void setDeviceProblem(DeviceProblem deviceProblem) {
        this.deviceProblem = deviceProblem;
        this.currentFlowchart = deviceProblem.getFlowchart();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Flowchart getCurrentFlowchart() {
        return currentFlowchart;
    }

    public void setCurrentFlowchart(Flowchart currentFlowchart) {
        this.currentFlowchart = currentFlowchart;
    }

    public enum Urgency {
        Low, Medium, High, Critical
    }
}
