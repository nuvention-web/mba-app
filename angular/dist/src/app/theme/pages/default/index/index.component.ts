import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { Helpers } from '../../../../helpers';
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import { forEach } from '@angular/router/src/utils/collection';
import { Response } from '@angular/http';

@Component({
    selector: "app-index",
    templateUrl: "./index.component.html",
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class IndexComponent implements OnInit, AfterViewInit {

    schools: any;
    allSchools: any;
    schoolsInfo: any = undefined;
    chosenValue: string;
    chosenDeadline: string;
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
        this._schools.getSchools().subscribe(d => {this.schools = d;});
    }


    deleteSchool(event, index, schoolName: string) {
        event.stopPropagation();
        this.schools.splice(index, 1);
        this.updated = new Date();
        this._schools.userDeleteSchool(schoolName);
    }

    ngOnInit() {
        this.getSchools();
    }

    
    ngAfterViewInit() {
        this._script.loadScripts('app-index',
            ['assets/app/js/dashboard.js']);

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



}
