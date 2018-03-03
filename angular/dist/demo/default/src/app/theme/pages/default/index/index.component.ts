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

    schools: any = undefined;
    allSchools: any = undefined;
    chosenValue: string;

    constructor(private _script: ScriptLoaderService, private _schools:SchoolsService) {
      this._schools.getSchools().subscribe(d => this.schools = d);
      this._schools.getAllSchools().subscribe(d => this.allSchools = d);
    }

    deleteSchool(schoolName: string) {
        this._schools.userDeleteSchool(schoolName);
    }
    ngOnInit() {
        this._schools.getSchools().subscribe(d => this.schools = d);
    }

    
    ngAfterViewInit() {
        this._script.loadScripts('app-index',
            ['assets/app/js/dashboard.js']);

    }

    notEmpty() {
        return this.schools != undefined && this.allSchools != undefined;
    }
    addSchool() {
        for (var i = 0; i < this.allSchools.length; i++) {
            if (this.allSchools[i].name === this.chosenValue) {
                this._schools.userAddSchool(this.allSchools[i].shortName).subscribe(
                    (response:Response) => {

                    }, (error:Response) => {
                        console.log(error);
                    }
                );
                this.schools.push(this.allSchools[i]);
                break;
            }
        }
    }

}
