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
    selector: 'app-profilePDF',
    templateUrl: './profilePDF.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class ProfilePDFComponent implements OnInit{
    schools: any;
    user: string;
    resumeUploadText = ""
    profilePDF: any;
    resumeUploadButtonDisabled = false;


    constructor(private _script: ScriptLoaderService, private _schools:SchoolsService) {
        this.getSchools();
        this.resumeUploadText = "Generate a PDF"

    }

    getSchools() {
        this._schools.getSchools().subscribe(d => {this.schools = d; this.profilePDF = d["profilePDF"]; this.user = d["name"]});
    }

    getDate() {
        if(this.profilePDF==null) {
            return "";
        }
        else{
            return this.profilePDF["date"]
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
    //
    // clickButton(){
    //     this.text="Clicked";
    //     this.disableUpload = true;
    // }
    //



    downloadPDF() {
        this._schools.downloadProfile(this.profilePDF["uuid"]);
    }


    generatePDF() {

        this.resumeUploadText = "Generating PDF";
        this.resumeUploadButtonDisabled = true;

        this._schools.generatePDF().subscribe(
            (response:Response) => {
                this._schools.getSchools(). subscribe(d => {
                    this.profilePDF = d["profilePDF"];
                    this.resumeUploadText = "Generate a PDF"
                    this.resumeUploadButtonDisabled = false;
                })
            }, (error:Response) => {
                this.resumeUploadText = "Generate a PDF"
                this.resumeUploadButtonDisabled = false;
            }
        );

    }



}



