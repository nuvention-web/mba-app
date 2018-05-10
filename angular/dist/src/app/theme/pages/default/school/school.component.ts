import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';


@Component({
    selector: 'app-essays',
    templateUrl: './school.component.html',
    encapsulation: ViewEncapsulation.None,
    styleUrls: ['./school.component.css'],
    providers: [SchoolsService]
})
export class SchoolComponent implements OnInit {
    school = "";
    schoolDetails: any = [];

    constructor(private route: ActivatedRoute, private _script: ScriptLoaderService, private _schools:SchoolsService) {
        this.route.params.subscribe( params =>
            this.school = params.school

        );
        this._schools.getSchoolDetails(this.school).subscribe(d => this.schoolDetails = d);
    }

    saveSchool(i) {
        console.log(this.schoolDetails.notes);
        this._schools.updateNote(this.school, this.schoolDetails.notes[i].noteID, this.schoolDetails.notes[i].contents, this.schoolDetails.notes[i].title);
    }

    ngOnInit() {
    }

    newNote() {
        let note = {title: "", contents:""};
        this.schoolDetails.notes.push(note);
        this._schools.addNote(this.school, "Contents", "Title");
    }

    delNote(note) {
        this._schools.deleteNote(this.school, note.noteID);
        this.schoolDetails.notes = this.schoolDetails.notes.filter(obj => obj !== note);
    }
}