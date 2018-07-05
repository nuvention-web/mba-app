///<reference path="../../../../../typings.d.ts"/>
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {BrowserModule, DomSanitizer} from '@angular/platform-browser';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import {Router} from "@angular/router";
import { Response } from '@angular/http';
import { Pipe, PipeTransform } from "@angular/core";


@Component({
    selector: 'app-resume',
    templateUrl: './resume.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class ResumeComponent implements OnInit{
    params = {};
    content = "";
    essay:any = {};
    allEssays = [];
    school = "";
    essayID = "";
    fileUpload = "";
    schoolDetails: any = [];
    draftID = "";
    draft = {};
    submitted: any = false;
    resumes = [];
    text = "";
    disableUpload = false;
    scoreResumeText = "";
    scoreResumeButtonDisabled = false;
    resumeUploadText = "";
    resumeUploadButtonDisabled = false;


    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private router:Router) {

        this._schools.getResumes(). subscribe(d => {
            this.resumes = d.resumes;
            if(d.resumesScored<3 || d.allowUnlimitedResumes){
                this.scoreResumeButtonDisabled = false;
                this.scoreResumeText="Score your resume";
            }
            else{
                this.scoreResumeButtonDisabled = true;
                this.scoreResumeText="Resume scoring limit reached";
            }
        })
        this.resumeUploadText = "Upload your resume";

    }

    getResumes(){
        console.log(this.resumes);
    }

    disabled(){
        return this.disableUpload;
    }
    uploadResume() {
        this.resumeUploadText = "Uploading resume";
        this.resumeUploadButtonDisabled = true;
        this._schools.uploadResume(this.fileUpload).subscribe(
            (response:Response) => {
                this._schools.getResumes(). subscribe(d => {
                    this.resumes = d.resumes;
                    this.resumeUploadText = "Upload your resume";
                    this.resumeUploadButtonDisabled = false;
                })
            }, (error:Response) => {
                this.resumeUploadText = "Upload your resume";
                this.resumeUploadButtonDisabled = false;
            }
        );
    }

    uploadFile(event) {
        let file = event.target.files[0];
        console.log(file); // You will see the file
        this.fileUpload = file;
        console.log(file.size)
        if(file!=null && file.size>2000000) {
            this.resumeUploadButtonDisabled=true;
            this.resumeUploadText="The filesize exceeds the 2MB limit. Only files upto 2MB can be uploaded. Please upload a smaller file."
        }
        else{
            this.resumeUploadButtonDisabled=false;
            this.resumeUploadText="Upload your resume"
        }
    }

    ngOnInit() {
    }

    ngAfterContentInit() {
    }

    ngAfterViewInit() {
    }

    ngAfterViewChecked() {
        this.initializePopover();
    }


    initializePopover() {
        (<any>$)('#resumeContainer').popover({
            selector: '[data-toggle="m-popover"]',
            placement: 'right',
            trigger: 'hover'
        });

    }

    clickButton(){
        this.text="Clicked";
        this.disableUpload = true;
    }



    displayStats(resume) {
        if(resume["analysisDone"]=="True"){
            return true;
        }
        return false;
    }

    scoreResume(resume) {
        this.scoreResumeButtonDisabled = true;
        this.scoreResumeText = "Resume scoring in progress";
        this._schools.scoreResume(resume["resumeID"]).subscribe(
            (response:Response) => {
                this._schools.getResumes(). subscribe(d => {
                    this.resumes = d.resumes;
                    this.scoreResumeText="Score your resume";
                    if(d.resumesScored<3 || d.allowUnlimitedResumes){
                        this.scoreResumeButtonDisabled = false;
                        this.scoreResumeText="Score your resume";
                    }
                    else{
                        this.scoreResumeButtonDisabled = true;
                        this.scoreResumeText="Resume scoring limit reached";
                    }
                })
            }, (error:Response) => {
                // this.scoreResumeText="Score your resume";
                // this.scoreResumeButtonDisabled = false;
            },
        );
    }

    downloadResume(resume) {
        this._schools.downloadResume(resume["resumeID"]);
    }


    deleteResume(resume) {
        this._schools.deleteResume(resume["resumeID"]).subscribe(
            (response:Response) => {
                this._schools.getResumes(). subscribe(d => {
                    this.resumes = d.resumes;
                })
            }, (error:Response) => {
            },
        );
    }




}



