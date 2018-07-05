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
    fileUpload = "";
    reviews = [];
    drafts = [];
    schoolDetails: any;
    profile: {"": ""};
    @Input()
    schoolInfo: any;
    component = "";

    contactName: string;
    contactDraft: string;
    contactEmail: string;
    contactComment: string;
    contactSuccess = false;

    sendForFeedbackDisabled = false;
    uploadButtonDisabled = false;
    uploadButtonString: string;

    draftTitle: string


    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private _profile: ProfileService, private router:Router) {
        this.schoolDetails = {notes: []};
        this.route.params.subscribe(params => {
            this.params = params;
            this.school = params.school;
            this.essayID = params.id;
            this.component = params.component;
        });
        this._schools.getEssay(this.school, this.essayID).subscribe(d => {this.essay = d; this.drafts = d["drafts"]});
        this._schools.getEssayReviews(this.school, this.essayID).subscribe(d => this.reviews = d.reviews);
        this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
        this._schools.getSchoolDetails(this.school).subscribe(d => this.schoolDetails = d);
        this._profile.getProfile().subscribe(p => {this.profile = p; delete this.profile['name']; delete this.profile['email']; delete this.profile['id']; delete this.profile['resumes']});
        this.uploadButtonString = "Upload";
    }

    ngOnInit() {
        var self = this;
        (<any>$)("#draft_editor").summernote({
            toolbar: [
                // [groupName, [list of button]]
                ['style', ['bold', 'italic', 'underline']],
                ['fontsize', ['fontsize']],
                ['height', ['height']]
            ],
            placeholder: 'Start typing here...',
            tabsize: 2,
            height: 200
        });
        (<any>$)("#essay-draft-dropdown").select2();
        (<any>$)("#notes-dropdown").select2();
        (<any>$)("#profile-dropdown").select2();

        var hash = window.location.hash;
        hash && (<any>$)('ul.nav a[href="' + hash + '"]').tab('show');

        (<any>$)('.nav-tabs a').click(function (e) {
            (<any>$)(this).tab('show');
            var scrollmem = (<any>$)('body').scrollTop() || (<any>$)('html').scrollTop();
            window.location.hash = this.hash;
            (<any>$)('html,body').scrollTop(scrollmem);
        });


    }

    public deleteDraft(draftID) {
        this._schools.deleteEssayDraft(this.school, this.essayID, draftID).subscribe(
            (response:Response) => {
                this._schools.getEssay(this.school, this.essayID).subscribe(d => {this.essay = d; this.drafts = d["drafts"]});
                this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
            }, (error:Response) => {
            }
        );
    }

    editDraft(draftID) {
        this.router.navigate(['/','school',this.school, 'edit', 'essay', this.essayID, draftID])
    }

    textEditorDraft(draft){
        if(draft["draftType"]=="CONTENTS"){
            return true;
        }
        return false;
    }

    public draftString(draft){
        if(draft["draftType"]=="CONTENTS"){
            return "Last modified on "+draft.date;
        }
        else{
            return "Uploaded on "+draft.date;
        }
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

    public downloadDraft(draftID) {
        this._schools.downloadEssayDraft(this.school, this.essayID, draftID);
    }

    profileKeys() {
        if (this.profile == null) {
            return [""];
        }
        return Object.keys(this.profile);
    }


    uploadFile(event) {
        let file = event.target.files[0];
            console.log(file); // You will see the file
            this.fileUpload = file;
             if(file!=null && file.size>2000000) {
                this.uploadButtonDisabled=true;
                this.uploadButtonString="The filesize exceeds the 2MB limit. Only files upto 2MB can be uploaded. Please upload a smaller file."
            }
            else{
                this.uploadButtonDisabled=false;
                this.uploadButtonString="Upload"
            }

    }

    uploadDraft() {
        this._schools.uploadEssayDraft(this.fileUpload, this.school, this.essayID).subscribe(
            (response:Response) => {
                this._schools.getEssay(this.school, this.essayID).subscribe(d => {this.essay = d; this.drafts = d["drafts"]});
                this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
                document.getElementById("openModalButton").click();
            }, (error:Response) => {
            }
        );
    }

    viewReviewInNewWindow(path) {
        var url = window.location.protocol + "//" + window.location.host + path
        window.open(url, '_blank')
    }

    saveDraft() {
        var essay_code = (<any>$)("#draft_editor").summernote('code');
        this._schools.saveEssayDraft(essay_code, this.school, this.essayID, this.draftTitle).subscribe(
            (response:Response) => {
                this._schools.getEssay(this.school, this.essayID).subscribe(d => {this.essay = d; this.drafts = d["drafts"]});
                this._schools.getAllEssays().subscribe(d => this.allEssays = this.transformJSON(d));
                (<any>$)('#myTab a[href="#drafts"]').tab('show');
                document.getElementById("openModalButton").click();
            }, (error: Response) => {

            }
        );
    }

    sendForReview() {
        this.sendForFeedbackDisabled = true;
        this._schools.sendForReview(this.school, this.essayID, this.contactDraft, this.contactEmail, this.contactName, this.contactComment).subscribe((response) => {
            this._schools.getEssayReviews(this.school, this.essayID).subscribe(d => this.reviews = d.reviews);
            this.contactSuccess = true;
            this.sendForFeedbackDisabled = false;
        }, (error) => {
            this.sendForFeedbackDisabled = false;
        });
    }

    formatDate(d) {
        return moment(d).format("LL");
    }




}
