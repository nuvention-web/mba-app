import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';


@Component({
    selector: 'app-essays',
    templateUrl: './school.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class SchoolComponent implements OnInit {
    school = "";
    schoolDetails: any = [];

    constructor(private route: ActivatedRoute, private _script: ScriptLoaderService, private _schools:SchoolsService) {
        this.route.params.subscribe( params =>
            this.school = params.school

        );
        this._schools.getSchoolDetails(this.school).subscribe(d => this.schoolDetails = d);
    }

    ngOnInit() {
    }
}