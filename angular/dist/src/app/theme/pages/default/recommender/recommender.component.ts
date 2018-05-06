import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import {Router} from "@angular/router";
import { Response } from '@angular/http';


@Component({
    selector: 'app-recommender',
    templateUrl: './recommender.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class RecommenderComponent implements OnInit {
    school = "";
    note:any = [];
    recommenderID = "";
    recommenderName = "";
    input = {};

    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private router:Router) {

        this.route.params.subscribe(params => {
                console.log(params);
                this.school = params.school
                this.recommenderID = params.recommenderID
                this.recommenderName = params.recommenderName

            }
        );
        this._schools.getRecommender(this.school, this.recommenderID).subscribe(d => this.note = d);
    }

    ngOnInit() {
        
    }

    goBack() {
        this.router.navigate(['/essays/' + this.school])
    }


    updateNote() {
        console.log(this.note);
        this._schools.updateRecommender(this.school, this.recommenderID, this.note.contents).subscribe(
            (response:Response) => {
                console.log(response)
                this.router.navigate(['/essays/' + this.school])
            }, (error:Response) => {
                console.log(error);
            }
        )

    }
}