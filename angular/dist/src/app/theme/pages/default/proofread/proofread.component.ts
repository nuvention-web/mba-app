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
    selector: 'app-proofread',
    templateUrl: './proofread.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class ProofreadComponent implements OnInit{
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
    proofReadText = "";
    proofReadButtonDisabled = false;


    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private router:Router) {

        this.route.params.subscribe(params => {
            this.params = params;
            this.school = params.school;
            this.essayID = params.essayID;
            this.draftID = params.draftID;
        });
        this._schools.getDraft(this.school, this.essayID, this.draftID). subscribe(d => {
            this.draft = d;
        });
        this.proofReadText = "Proofread your essay!"
    }


    ngOnInit() {
    }

    ngAfterViewInit() {
        this.initializePopover();
    }

    initializePopover() {
        (<any>$)('#grammarContainer').popover({
            selector: '[data-toggle="m-popover"]',
            placement: 'right',
            trigger: 'hover'
        });
    }

    public getSentimentScore() {

        if(this.draft["analysisRun"]=="True") {
            return this.draft["sentimentScore"]
        }

        return "";


    }

    public displayAnalysisButton() {
        if(this.draft["analysisRun"]=="True") {
            return false;
        }

        return true;

    }

    public displayProofReadButton() {
        if(this.draft["grammarCheckRun"]=="True") {
            return false;
        }

        return true;

    }

    public runProofRead() {
        this.proofReadText = "Proofreading in progress"
        this.proofReadButtonDisabled = true;
        this._schools.runProofRead(this.school, this.essayID, this.draftID).subscribe(
            (response: Response) => {
                this._schools.getDraft(this.school, this.essayID, this.draftID).subscribe(d => {
                    this.draft = d;
                    (<any>$)('[data-toggle="m-popover"]').popover();
                });
            }, (error: Response) => {

            }
        );
    }




    public runAnalysis() {
        this._schools.runAnalysis(this.school, this.essayID, this.draftID).subscribe(
            (response: Response) => {
                this._schools.getEssay(this.school, this.essayID).subscribe(d => this.essay = d);
                this._schools.getDraft(this.school, this.essayID, this.draftID).subscribe(d => this.draft = d)
            }, (error: Response) => {

            }
        );
    }

}

@Pipe({name: 'safeHtml'})
export class SafeHtmlPipe implements PipeTransform {
    constructor(private sanitized: DomSanitizer) {
    }
    transform(value: string) {
        return this.sanitized.bypassSecurityTrustHtml(value);
    }
}

export class matProgressBar{

}

