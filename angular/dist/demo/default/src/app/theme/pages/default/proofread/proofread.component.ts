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
    fileUpload = ""
    schoolDetails: any = [];
    draftID = "";


    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private router:Router) {

        this.route.params.subscribe(params => {
            this.params = params;
            this.school = params.school;
            this.essayID = params.essayID;
            this.draftID = params.draftID;
        });
        this._schools.getEssay(this.school, this.essayID).subscribe(d => this.essay = d);
    }


    ngOnInit() {

    }

    public findEssay() {
        for(var draft of this.essay["drafts"]){
            if(this.draftID==draft["id"]) {
                return draft["grammarCheck"]
            }
        }
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

