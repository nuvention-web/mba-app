import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {SchoolsService} from "../../../../_services/schools.service";
import {ScriptLoaderService} from "../../../../_services/script-loader.service";

@Component({
    selector: 'app-blank',
    templateUrl: './testprep.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class TestPrepComponent implements OnInit {

    scores : any;
    schools = [];

    constructor(private _script: ScriptLoaderService, private _schools:SchoolsService) {
        this.getScores();
    }

    getScores() {
        this._schools.getScores().subscribe(d => {this.scores = d; this.schools = d["schools"];});
    }


    ngOnInit() {
    }
}