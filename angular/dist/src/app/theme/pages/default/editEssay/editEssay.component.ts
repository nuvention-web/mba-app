///<reference path="../../../../../typings.d.ts"/>
import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import {Router} from "@angular/router";
import { Response } from '@angular/http';
import * as moment from 'moment';
import {ProfileService} from "../../../../_services/profile.service";
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';


@Component({
    selector: 'app-essay',
    templateUrl: './editEssay.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class EditEssayComponent implements OnInit {
    params = {};
    content = "";
    essay:any = {};
    allEssays = [];
    isCollapsed = false;
    school = "";
    draftID = "";
    essayID = "";
    fileUpload = "";
    draft = {};
    reviews = [];
    drafts = [];
    schoolDetails: any;
    profile: {"": ""};
    @Input()
    schoolInfo: any;

    contactName: string;
    contactDraft: string;
    contactEmail: string;
    contactComment: string;
    contactSuccess = false;


    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private _profile: ProfileService, private router:Router) {
        this.schoolDetails = {notes: []};
        this.route.params.subscribe(params => {
            this.params = params;
            this.school = params.school;
            this.essayID = params.id;
            this.draftID = params.draftID;
        });
        this._schools.getDraft(this.school, this.essayID, this.draftID).subscribe(d => {
            this.draft = d;
        });
        this._schools.getEssay(this.school, this.essayID).subscribe(d => {this.essay = d; this.drafts = d["drafts"]});
        this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
        this._schools.getSchoolDetails(this.school).subscribe(d => this.schoolDetails = d);
        this._profile.getProfile().subscribe(p => {this.profile = p; delete this.profile['name']; delete this.profile['email']; delete this.profile['id']; delete this.profile['resumes']});
    }

    ngOnInit() {
        var self = this;
        console.log("Init");
        (<any>$)("#draft_editor").summernote({
            toolbar: [
                // [groupName, [list of button]]
                ['style', ['bold', 'italic', 'underline']],
                ['fontsize', ['fontsize']],
                ['height', ['height']]
            ],
            placeholder: 'Start typing here...',
            tabsize: 2,
            height: 400
        })
        ;
        (<any>$)("#essay-draft-dropdown").select2();
        (<any>$)("#notes-dropdown").select2();
        (<any>$)("#profile-dropdown").select2();

        this._schools.getDraft(this.school, this.essayID, this.draftID).subscribe(d => {
            this.draft = d;
            console.log(this.draft);
            (<any>$)("#draft_editor").summernote('editor.pasteHTML', this.draft["contents"]);
        });

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


    saveDraft() {
        var essay_code = (<any>$)("#draft_editor").summernote('code');
        this._schools.editEssayDraft(essay_code, this.school, this.essayID, this.draftID).subscribe(
            (response:Response) => {
                document.getElementById("openModalButton").click();
                // this.router.navigate(['/','school',this.school, 'essay', this.essayID], {fragment: 'drafts'} )
            }, (error: Response) => {

            }
        );
    }

    saveAndContinue() {
        var essay_code = (<any>$)("#draft_editor").summernote('code');
        this._schools.editEssayDraft(essay_code, this.school, this.essayID, this.draftID).subscribe(
            (response:Response) => {
                this.router.navigate(['/','school',this.school, 'essay', this.essayID], {fragment: 'drafts'} )
            }, (error: Response) => {

            }
        );
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
        if(this.schoolDetails.notes) {
            this.schoolDetails.notes.forEach(function (note) {
                if(note.title == requestedNote) {
                    theNote = note;
                    return
                }
            });
        }

        return theNote;


    }

    profileKeys() {
        if (this.profile == null) {
            return [""];
        }
        return Object.keys(this.profile);
    }


    public findProfile(requestedQuestion) {
        let theProfile = {}
        if (this.profile) {
            var data = this.profile[requestedQuestion];
            if (data instanceof Array) {
                var res = data.join('</p><p>');
                return '<p>' + res + '</p>';
            }
            return data;
        }
        return theProfile;
    }


}
