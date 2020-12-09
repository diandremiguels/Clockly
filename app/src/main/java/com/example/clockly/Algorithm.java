package com.example.clockly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// An Algorithm will be able to take a list of input tasks and times
// and fabricate a schedule that fits all of the tasks appropriately.
public class Algorithm {

    private final int LONG_BREAK = 20;
    private final int SHORT_BREAK = 5;
    private final int MINUTES_IN_DAY = 1440;
    private final int MINUTES_IN_HOUR = 60;
    private final int SMALL_CHUNK_SIZE = 30;
    private final int MAX_TASK_SIZE = 60;

    // This is a list of all the (nonessential) tasks that need to be done in the day.
    // The time to accomplish a task is mapped to a list of all the tasks that take that amount of time to finish.
    private Map<Integer, List<String>> tasks;

    // This is a map of the daily schedule. A particular time in the day (in minutes) may be mapped to a list of strings that contains
    // the name of the task and an indicator for whether or not the activity is starting or ending.
    private Map <Integer, List<String>> schedule;

    // Creates a new Algorithm object with an empty map of tasks and an empty schedule map
    public Algorithm() {
        tasks = new TreeMap<Integer, List<String>>();
        schedule = new TreeMap<Integer, List<String>>();
        // The below lines are for testing purposes
        scheduleActivity("sleep", 1380, 480);
        scheduleActivity("breakfast", 510, 540);
        scheduleActivity("lunch", 720, 750);
        scheduleActivity("dinner", 1140, 1170);
    }

    // Might need to change what goes in the database so that removing actually works...

    // Adds specified task to the schedule, mapping the task to the times it begins and ends at.
    // Also specifies whether the task starts or ends at that time.
    // Can be used to automatically schedule essential activities like sleep or meal times.
    // Also used to schedule nonessential activities like homework in a scheduling algorithm.
    public void scheduleActivity(String taskName, int startTime, int endTime){
        this.schedule.put(startTime, new ArrayList<>(Arrays.asList(taskName, "start"))); // technically forbidden feature but will need to figure something else out later
        this.schedule.put(endTime, new ArrayList<>(Arrays.asList(taskName, "end")));
    }

    // Adds task to map, mapping tasks to the amount of time it would take to complete.
    public void addTask(int time, String task) {
        List<String> currentTasks = (tasks.containsKey(time))? tasks.get(time) : new ArrayList<>(); //ternary operator
        currentTasks.add(task);
        tasks.put(time, currentTasks);
    }

    // Prints each saved task with its associated time to complete.
    // Prints task in order from least to greatest time to complete.
    // NEED THIS TO CHANGE TO RETURN A LIST OF STRINGS INSTEAD
    public void printTasks() {
        System.out.println("\nHere are a list of your daily tasks:");
        for (int time : tasks.keySet()){
            for (String task : tasks.get(time)){
                System.out.println(task + ": " + time + " minutes");
            }
        }
    }

    // Returns a string with the given time in minutes converted to 12 hour standard format.
    public String minutesToStandard(int time){
        int hours = time / MINUTES_IN_HOUR;
        boolean isPM = hours >= 12;
        if (hours >= 13){
            hours -= 12;
        }
        if (hours == 0){
            hours = 12;
        }
        int minutes = time % MINUTES_IN_HOUR;
        return "" + hours +  ":" + ((minutes < 10)? "0" : "") + minutes + ((isPM)? " PM" : " AM");
    }

    // Prints mapped schedule to the console, listing what tasks begin or end at each time.
    public void printSchedule(){
        System.out.println("\nHere's your schedule:");
        for (int time : this.schedule.keySet()){
            System.out.println(minutesToStandard(time) + "-> " + schedule.get(time).get(1) + " " + schedule.get(time).get(0));
        }
    }

    // Takes current list of tasks and maps each task to the specific times the task should start or end at.
    // Between every tasks, a long break is automatically factored in.
    // Prioritizes finishing smaller tasks before starting larger ones.
    // If a task is exceptionally large, it is split into smaller pieces, with a short break in between each smaller task.
    // If a task cannot fit into the schedule, then outputs a message that says it cannot
    public void mapSchedule(){
        scheduleHelper(this.tasks);
    }

    // Helper method for mapSchedule()
    // If the task is large, it splits it into smaller pieces and maps those pieces into the schedule. Otherwise, it continues as normal.
    // Checks if task fits into schedule once break times are accounted for, then adds task into time slot.
    private void scheduleHelper(Map<Integer, List<String>> tasks){
        for (int time : tasks.keySet()){
            for (String task : tasks.get(time)){
                if (time >= MAX_TASK_SIZE){
                    Map<Integer, List<String>> splitTasks = this.split(task, time);
                    scheduleHelper(splitTasks);
                } else {
                    int newStart = -1;
                    int newEnd = -1;
                    if (schedule.keySet().isEmpty()){
                        this.scheduleActivity(task, 0, time);
                    } else {
                        for (int previousTime : this.schedule.keySet()){
                            if (schedule.get(previousTime).contains("end") && newStart == -1){
                                int possibleStartTime = previousTime + ((schedule.get(previousTime).contains(task))? this.SHORT_BREAK : this.LONG_BREAK);
                                int possibleEndTime = possibleStartTime + time;
                                boolean fitsTimeSlot = true;
                                for (int otherTime : this.schedule.keySet()){
                                    if (this.schedule.get(otherTime).contains("start"));{
                                        if (possibleStartTime < otherTime && possibleEndTime > otherTime - ((schedule.get(otherTime).contains(task))? this.SHORT_BREAK : this.LONG_BREAK)){
                                            fitsTimeSlot = false;
                                        }
                                    }
                                }
                                if (fitsTimeSlot){
                                    newStart = possibleStartTime;
                                    newEnd = possibleEndTime;
                                }
                            }
                        }
                        if (newStart != -1 && newEnd < this.MINUTES_IN_DAY){
                            this.scheduleActivity(task, newStart, newEnd);
                        } else {
                            System.out.println(time + " minutes of " + task + " doesn't fit into the schedule");
                        }
                    }
                }
            }
        }
    }


    // Splits a large task into smaller 30-minute chunks and one other chunk with the remaining time.
    // Then, returns a map where the task is mapped to the time it takes to finish it.
    private Map<Integer, List<String>> split(String task, int time) {
        Map<Integer, List<String>> smallerTasks = new HashMap<Integer, List<String>>();
        int numBreaks = time / SMALL_CHUNK_SIZE;
        int leftoverTime = time % SMALL_CHUNK_SIZE;
        List<String> taskRepetitions = new ArrayList<>();
        for (int i = 0; i < numBreaks; i++){
            taskRepetitions.add(task);
        }
        List<String> leftoverRepetition = new ArrayList<>();
        leftoverRepetition.add(task);
        if (numBreaks > 0){
            smallerTasks.put(SMALL_CHUNK_SIZE, taskRepetitions);
        }
        if (leftoverTime > 0){
            smallerTasks.put(leftoverTime, leftoverRepetition);
        }
        return smallerTasks;
    }


    // To store in database, must turn each key / value set into a single string
    // to use database values, must turn each string into a key / value set
    // only need to store tasks in database because algorithm will calculate schedule
    // every time it displays, won't be saved

    public void taskStringToMap(String input){
        String[] words = input.split(" ");
        if (words.length >= 2){
            // assumes string will be in form 'time' + 'name'
            int time = Integer.parseInt(words[0]);
            String taskName = "";
            for (int i = 1; i < words.length; i++){
                taskName += words[i] + " ";
            }
            this.addTask(time, taskName.trim());
        }
    }

    public List<String> taskMapToStringList(){
        List<String> tasks = new ArrayList<>();
        for (int time : this.tasks.keySet()){
            tasks.add(time + this.tasks.get(time).get(0) + this.tasks.get(time).get(1));
            // format is "30 homework start"
        }
        return tasks;
    }

    public List<String> scheduleMapToList(){
        List<String> output = new ArrayList<>();
        for (int time : this.schedule.keySet()){
            String line = minutesToStandard(time) + " -> " + this.schedule.get(time).get(1) + " " + this.schedule.get(time).get(0);
            output.add(line);
        }
        return output;
    }
}


