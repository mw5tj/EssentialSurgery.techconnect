package org.centum.techconnect.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
    //Stack of previous flowcharts shown
    private Stack<Flowchart> stack = new Stack<>();
    private List<Flowchart> history = new LinkedList<>();

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

    public void advanceTo(Flowchart newFlowchart) {
        stack.push(this.currentFlowchart);
        this.currentFlowchart = newFlowchart;
        history.add(newFlowchart);
    }

    public void goBack() {
        if (hasPrevious()) {
            this.currentFlowchart = stack.pop();
            history.add(this.currentFlowchart);
        }
    }

    public boolean hasPrevious() {
        return stack.size() > 0;
    }

    public enum Urgency {
        Low, Medium, High, Critical
    }
}
