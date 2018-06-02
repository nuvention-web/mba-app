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
    selector: 'app-gettingStarted',
    templateUrl: './gettingStarted.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class GettingStartedComponent{
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

        this.scoreResumeText="Score your resume";
        this.resumeUploadText = "Upload your resume";

    }




}



