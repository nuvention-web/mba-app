import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { Location } from '@angular/common';
import { Helpers } from '../../../../../../../helpers';
import { ScriptLoaderService } from '../../../../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../../../../_services/schools.service';
import { FormsModule } from '@angular/forms'; 

@Component({
    selector: "app-widgets-bootstrap-select",
    templateUrl: "./widgets-bootstrap-select.component.html",
    encapsulation: ViewEncapsulation.None,
})
export class WidgetsBootstrapSelectComponent implements OnInit, AfterViewInit {

    allSchools: any = []
    chosenValue: string;
    constructor(private _script: ScriptLoaderService, private _location: Location, 
        private _schools:SchoolsService) {
        this._schools.getAllSchools().subscribe(d => {
            this.allSchools = d; 
            this.chosenValue = this.allSchools[0].name;
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
                console.log(this.chosenValue);
                console.log(this.allSchools[i].shortName);
                this._schools.userAddSchool(this.allSchools[i].shortName);
            }
        }
    }

}