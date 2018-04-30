///<reference path="../../../../../typings.d.ts"/>
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import {Router} from "@angular/router";
import { Response } from '@angular/http';

@Component({
    selector: 'app-essay',
    templateUrl: './essay.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class EssayComponent implements OnInit {
    params = {};
    content = "";
    essay:any = {};
    allEssays = [];
    school = "";
    essayID = "";
    fileUpload = ""
    schoolDetails: any = [];


    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private router:Router) {

        this.route.params.subscribe(params => {
            this.params = params;
            this.school = params.school;
            this.essayID = params.id;
        });
        this._schools.getEssay(this.school, this.essayID).subscribe(d => this.essay = d);
        this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
        this._schools.getSchoolDetails(this.school).subscribe(d => this.schoolDetails = d);
    }

    ngOnInit() {
        var self = this;
        (<any>$)("#draft_editor").summernote({
            placeholder: 'Start typing here...',
            tabsize: 2,
            height: 200
        });
        (<any>$)("#essay-draft-dropdown").select2();
        (<any>$)("#notes-dropdown").select2();
    }

    public deleteDraft(draftID) {
        this._schools.deleteEssayDraft(this.school, this.essayID, draftID).subscribe(
            (response:Response) => {
                this._schools.getEssay(this.school, this.essayID).subscribe(d => this.essay = d);
                this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
            }, (error:Response) => {
            }
        );
    }

    transformJSON(d) {
        var list = [];
        Object.keys(d).forEach(function (key) {
            if (Object.keys(d[key]).length !== 0) {
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
        this.allEssays.forEach(function (school) {
            school.essays.forEach(function (essay) {
                if (essay.draftName == draft) {
                    theEssay = essay;
                    return;
                }
            });
        });
        return theEssay;
    }

    public findNote(requestedNote) {
        var theNote = {};
        var notes=[];

        notes = this.schoolDetails.notes;
        notes.forEach(function (note) {
            console.log("requested note "+requestedNote)
            //console.log(note)
            if(note.title == requestedNote) {
                theNote = note;
                console.log("Found "+theNote)
                return
            }
        });

        return theNote;


    }

    public downloadDraft(draftID) {
        this._schools.downloadEssayDraft(this.school, this.essayID, draftID);
    }


    uploadFile(event) {
        let file = event.target.files[0];
            console.log(file); // You will see the file
            this.fileUpload = file;
    }

    uploadDraft() {
        this._schools.uploadEssayDraft(this.fileUpload, this.school, this.essayID).subscribe(
            (response:Response) => {
                this._schools.getEssay(this.school, this.essayID).subscribe(d => this.essay = d);
                this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
                document.getElementById("openModalButton").click();
            }, (error:Response) => {
            }
        );
    }

    saveDraft() {
        this._schools.saveEssayDraft("yo", this.school, this.essayID).subscribe(
            (response:Response) => {
                this._schools.getEssay(this.school, this.essayID).subscribe(d => this.essay = d);
                this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
            }, (error: Response) => {

            }
        );
    }


}
