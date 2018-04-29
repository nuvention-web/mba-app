import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';
import {Router} from "@angular/router";
import { Response } from '@angular/http';


@Component({
    selector: 'app-notes',
    templateUrl: './notes.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [SchoolsService]
})
export class NotesComponent implements OnInit {
    school = "";
    note:any = [];
    noteID = "";
    action = "";
    input = {};

    constructor(private route:ActivatedRoute, _script:ScriptLoaderService, private _schools:SchoolsService,
                private router:Router) {

        this.route.params.subscribe(params => {
                console.log(params);
                this.school = params.school;
                this.noteID = params.noteID;
                this.action = params.action;

            }
        );
        this._schools.getNote(this.school, this.noteID).subscribe(d => this.note = d);
    }

    ngOnInit() {
        
    }

    goBack() {
        this.router.navigate(['/school/' + this.school])
    }


    updateNote() {
        if (this.action == "edit") {
            this._schools.updateNote(this.school, this.noteID, this.note.contents, this.note.title).subscribe(
                (response:Response) => {
                    this.router.navigate(['/school/' + this.school])
                }, (error:Response) => {
                }
            )
        } else {
            this._schools.addNote(this.school, this.note.contents, this.note.title).subscribe(
                (response:Response) => {
                    this.router.navigate(['/school/' + this.school])
                }, (error:Response) => {
                }
            )
        }

    }
}