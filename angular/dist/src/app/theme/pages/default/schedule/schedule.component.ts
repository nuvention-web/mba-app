import { Component, OnInit, ViewEncapsulation, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import {TasksService} from "../../../../_services/tasks.service";
import {ScriptLoaderService} from "../../../../_services/script-loader.service";


declare var $:JQueryStatic;

@Component({
    selector: 'app-schedule',
    templateUrl: './schedule.component.html',
    encapsulation: ViewEncapsulation.None,
    styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit, AfterViewInit {

    @ViewChild('newEventModal') newEventModal : ElementRef;
    @ViewChild('updateEventModal') updateEventModal : ElementRef;
    @ViewChild('newDate') newDate: ElementRef;
    @ViewChild('updateDate') updateDate: ElementRef;
    deadlines = [];
    activities = [];
    activity = {name: '', description: '', date: '', taskID: '', completed: false};
    colors = ['info', 'warning', 'brand', 'success', 'danger', 'accent', 'focus', 'primary', 'light'];
    colorsSize = 8;
    constructor(private tasks: TasksService, private _script: ScriptLoaderService) {
        this.refresh();
    }

    ngOnInit() {

    }

    ngAfterViewInit() {
        this._script.loadScripts('app-schedule',
            ['assets/demo/default/custom/components/forms/widgets/bootstrap-datepicker.js']);
    }

    refresh() {
        this.tasks.getTasksAndDeadline().subscribe(d => {
            this.deadlines = d.deadlines;
            this.activities = d.tasks;
            this.deadlines.forEach(d => d.name = d.name.slice(0, -9));
        });
    }

    updateDatetime() {
        console.log(this.newDate.nativeElement.value);
    }

    newActivity() {
        this.activity = {name: '', description: '', date: '', taskID: '', completed: false};
        this.showForm(this.newEventModal);
    }

    getCompletedActivities(){
        var completedActivities = [];
        for(let i=0; i<this.activities.length; i++){
            if(this.activities[i].completed==true)
            {
                completedActivities.push(this.activities[i])
            }
        }
        return completedActivities;
    }

    getIncompleteTasks(){
        var incompleteTasks = [];
        for(let i=0; i<this.activities.length; i++){
            if(this.activities[i].completed==false)
            {
                incompleteTasks.push(this.activities[i])
            }
        }
        return incompleteTasks;
    }

    completedButtonText(a) {
        if(a.completed==true){
            return "Mark as incomplete";
        }
        else{
            return "Mark as complete";
        }

    }



    addActivity() {
        this.activity.date = this.newDate.nativeElement.value;
        this.tasks.addTask(this.activity).subscribe(d => this.refresh());
        this.activity = {name: '', description: '', date: '', taskID: '', completed: false};
    }

    modifyActivity(a) {
        this.activity.name = a.name;
        this.activity.description = a.details;
        this.activity.taskID = a.id;
        this.activity.date = a.date;
        this.activity.completed = a.completed;
        this.showForm(this.updateEventModal);
    }

    updateActivity() {
        this.activity.date = this.updateDate.nativeElement.value;
        this.tasks.updateTask(this.activity).subscribe(d => this.refresh());
        this.activity = {name: '', description: '', date: '', taskID: '', completed: false};
    }

    completeTask() {
        this.activity.date = this.updateDate.nativeElement.value;
        this.tasks.completeTask(this.activity).subscribe(d => this.refresh());
        this.activity = {name: '', description: '', date: '', taskID: '', completed: false};
    }


    deleteTask() {
        this.activity.date = this.updateDate.nativeElement.value;
        this.tasks.deleteTask(this.activity).subscribe(d => this.refresh());
        this.activity = {name: '', description: '', date: '', taskID: '', completed: false};
    }



    showForm(modal) {
        (<any>$(modal.nativeElement)).modal('show');
    }

    hideForm(modal) {
        (<any>$(modal.nativeElement)).modal('hide');
    }

}