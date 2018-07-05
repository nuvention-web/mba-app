import { Component, OnInit, ViewEncapsulation, AfterViewInit, ViewChild, ElementRef} from '@angular/core';
import { Helpers } from '../../../../helpers'
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import { forEach } from '@angular/router/src/utils/collection';
import { Response } from '@angular/http';

declare var $:JQueryStatic;

@Component({
    selector: "app-index",
    templateUrl: "./index.component.html",
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class IndexComponent implements OnInit, AfterViewInit {

    @ViewChild('deletedModal') deletedModal: ElementRef;
    @ViewChild('deadlineModal') deadlineModal: ElementRef;

    schools: any;
    allSchools: any;
    chosenValue: string;
    chosenDeadlineSchool: string = null;
    chosenDeadline: string = null;
    deadlineList = [];
    deletedSchool: string = null;
    deletedIndex: number = -1;
    user: string;
    updated;

    constructor(private _script: ScriptLoaderService, private _schools:SchoolsService) {
        this.getSchools();
        this.getSchoolsInfo();
        this._schools.getAllSchools().subscribe(d => {this.allSchools = d; console.log(d)});

    }

    getSchoolsInfo() {
        let i = 0;
        let j = 1;
        // this._schools.getSchoolInfos().subscribe(d => {
        //     this.schoolsInfo = d;
        //     for (i = 0; i < this.schoolsInfo.length; i++) {
        //         j = 1;
        //         this.schoolsInfo[i]['Deadline'] = [];
        //         while (this.schoolsInfo[i]['Deadline' + j] != null && this.schoolsInfo[i]['Deadline' + j] !== '') {
        //             this.schoolsInfo[i]['Deadline'].push(this.schoolsInfo[i]['Deadline' + j]);
        //             j++;
        //         }
        //     }
        // });
    }

    getSchools() {
        this._schools.getSchools().subscribe(d => {this.schools = d["schools"]; this.user = d["name"]});
    }


    confirmDeleteSchool(event, index, schoolName) {
        event.stopPropagation();
        this.deletedIndex = index;
        this.deletedSchool = schoolName;
        console.log(this.deletedModal);
        (<any>$(this.deletedModal.nativeElement)).modal('show');


    }
    deleteSchool(event) {
        event.stopPropagation();
        this.schools.splice(this.deletedIndex, 1);
        this.updated = new Date();
        this._schools.userDeleteSchool(this.deletedSchool);
        this.deletedIndex = -1;
        this.deletedSchool = null;
        (<any>$(this.deletedModal.nativeElement)).modal('hide');
    }

    getUser() {
        if(this.user!=null) {
            return this.user.split(' ')[0];
        }
        else{
            return "";
        }
    }

    ngOnInit() {
        this.getSchools();
    }

    
    ngAfterViewInit() {
        this._script.loadScripts('app-index',
            ['assets/app/js/dashboard.js']);

    }

    chooseDeadline(event, schoolName) {
        event.stopPropagation();
        this._schools.getSchoolInfos(schoolName).subscribe(d => this.deadlineList = this.getDeadline(d));
        this.chosenDeadlineSchool = schoolName;
        (<any>$(this.deadlineModal.nativeElement)).modal('show');
    }

    getDeadline(schoolInfo) {
        let i = 1;
        let name = 'round' + i.toString() + "Deadline";
        let res = [];

        if(schoolInfo["round0Deadline"]!=null){
            res.push(schoolInfo["round0Deadline"]);
        }

        while (schoolInfo[name] != null && schoolInfo[name] !== '') {
            res.push(schoolInfo[name]);
            i++;
            name = 'round' + i.toString() + "Deadline";
        }

        return res;
    }

    addDeadline() {
        (<any>$(this.deadlineModal.nativeElement)).modal('hide');
        this._schools.updateDeadline(this.chosenDeadlineSchool, this.chosenDeadline).subscribe(
            d => {
                this.getSchools();
                this._schools.getAllSchools().subscribe(d => {this.allSchools = d; console.log(d)});
            });
        this.deadlineList = [];
        this.chosenDeadlineSchool = null;
        this.chosenDeadline = null;
    }

    notEmpty() {
        return this.schools != undefined && this.allSchools != undefined;
    }

    addSchool() {
        if (this.chosenValue === "blank") {
            return;
        }
        let name = this.chosenValue;
        this._schools.userAddSchool(name).subscribe(
            (response: Response) => {
                this.getSchools();
                this._schools.getSchoolDetails(name).subscribe(d => {
                    d.deadline = this.chosenDeadline;
                    this._schools.updateSchoolDetails(name, d);
                });
            }, (error: Response) => {
            }
        );
        this.chosenValue = "blank";
    }

    getSchoolDeadline(school) {
        if(school.deadline==null) {
            return "Target Deadline not set";
        }
        return "Deadline " + school.deadline;
    }



}
