import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { Location } from '@angular/common';
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import { FormsModule } from '@angular/forms';
import {Router} from "@angular/router";
import { Response } from '@angular/http';


@Component({
    selector: "app-add-school",
    templateUrl: "./addschool.component.html",
    encapsulation: ViewEncapsulation.None,
})
export class AddSchoolComponent implements OnInit, AfterViewInit {

    allSchools: any = []
    userSchools: any = []
    schoolsRemain: any = []
    chosenValue: string;
    constructor(private _script: ScriptLoaderService, private _location: Location, 
        private _schools:SchoolsService, private router:Router) {
        this._schools.getAllSchools().subscribe(d => {
            this.allSchools = d; 
        });
        this._schools.getSchools().subscribe(d => {
            this.userSchools = d;
        });
    }
    ngOnInit() {

    }
    ngAfterViewInit() {
        this._script.loadScripts('app-widgets-bootstrap-select',
            ['assets/demo/default/custom/components/forms/widgets/bootstrap-select.js']);
    }

    backClicked() {
        this._location.back();  
    }

    addSchool() {
        console.log(this.chosenValue);
        for (var i = 0; i < this.allSchools.length; i++) {
            if (this.allSchools[i].name === this.chosenValue) {
                this._schools.userAddSchool(this.allSchools[i].shortName).subscribe(
                    (response:Response) => {
                        console.log(response)
                        this.router.navigate(['/index.html'])
                    }, (error:Response) => {
                        console.log(error);
                    }
                )
            }
        }
    }

}