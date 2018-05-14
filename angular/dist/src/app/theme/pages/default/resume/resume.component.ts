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


    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private router:Router) {

        this._schools.getResumes(). subscribe(d => {
            this.resumes = d.resumes;
        })
        this.scoreResumeText="Score your resume";
    }

    getResumes(){
        console.log(this.resumes);
    }

    disabled(){
        return this.disableUpload;
    }
    uploadResume() {
        this._schools.uploadResume(this.fileUpload).subscribe(
            (response:Response) => {
                this._schools.getResumes(). subscribe(d => {
                    this.resumes = d.resumes;
                })
                // document.getElementById("openModalButton").click();
            }, (error:Response) => {
            }
        );
    }

    uploadFile(event) {
        let file = event.target.files[0];
        console.log(file); // You will see the file
        this.fileUpload = file;
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
        (<any>$)('#grammarContainer').popover({
            selector: '[data-toggle="m-popover"]',
            placement: 'right',
            trigger: 'hover'
        });

    }

    clickButton(){
        this.text="Clicked";
        this.disableUpload = true;
    }



    public displayStats(resume) {
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
                    this.scoreResumeButtonDisabled = false;
                })
            }, (error:Response) => {
                this.scoreResumeText="Score your resume";
                this.scoreResumeButtonDisabled = false;
            },
        );
    }




}



