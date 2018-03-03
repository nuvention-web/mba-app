import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { Helpers } from '../../../../helpers';
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';

@Component({
    selector: "app-index",
    templateUrl: "./index.component.html",
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class IndexComponent implements OnInit, AfterViewInit {

    schools: any = [];

    constructor(private _script: ScriptLoaderService, private _schools:SchoolsService) {
      this._schools.getSchools().subscribe(d => this.schools = d);
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

}
