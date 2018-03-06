///<reference path="../../../../../typings.d.ts"/>
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';

@Component({
    selector: 'app-essay',
    templateUrl: './essay.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class EssayComponent implements OnInit {
    params = {};
    content = "";
    essay: any = {};
    allEssays = [];
    school = "";
    essayID = "";

    constructor(private route: ActivatedRoute, _script: ScriptLoaderService, private _schools:SchoolsService) {

        this.route.params.subscribe( params => {
            this.params = params;
            this.school = params.school;
            this.essayID = params.id;
        });
        this._schools.getEssay(this.school, this.essayID).subscribe(d => this.essay = d);
        this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));

    }

    ngOnInit() {
        var self = this;
        (<any>$)("#draft_editor").summernote({
            placeholder: 'Start typing here...',
            tabsize: 2,
            height: 200
        });
        (<any>$)("#essay-draft-dropdown").select2();
    }

    public deleteDraft(draftID) {
        this._schools.deleteEssayDraft(this.school, this.essayID, draftID);
    }

    transformJSON(d) {
        var list = [];
        Object.keys(d).forEach(function(key) {
            if(Object.keys(d[key]).length !== 0) {
                d[key].essays = [];
                Object.keys(d[key]).forEach(function (k) {
                    if (k !== "essays") {
                        d[key].essays.push(d[key][k]);
                    }
                });
                d[key].school = key;
                list.push(d[key]);
            }
        });
        return list;
    }

    public findEssay(draft) {
        var theEssay = {};
        this.allEssays.forEach(function(school) {
            school.essays.forEach(function(essay) {
               if (essay.draftName == draft) {
                   theEssay = essay;
                   return;
               }
            });
        });
        return theEssay;
    }
}
