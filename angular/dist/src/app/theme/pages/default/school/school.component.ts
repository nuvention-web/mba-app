import { DomSanitizer} from '@angular/platform-browser';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { SchoolsService } from '../../../../_services/schools.service';


const schoolImageDir = "../../assets/app/media/img/schools/";

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
    essays = [];
    schoolInfo: any = null;

    constructor(private route: ActivatedRoute, private _script: ScriptLoaderService, private _schools:SchoolsService, private _sanitizer: DomSanitizer) {
        this.route.params.subscribe( params => {
                this.school = params.school;
                this._schools.getSchoolInfos(this.school).subscribe(d => this.getSchoolInfo(d));
            }
        );
        this._schools.getSchoolDetails(this.school).subscribe(d => {this.schoolDetails = d; console.log(d)});
    }

    saveSchool(i) {
        this._schools.updateNote(this.school, this.schoolDetails.notes[i].noteID, this.schoolDetails.notes[i].contents, this.schoolDetails.notes[i].title);
    }

    getSchoolInfo(info) {
        this.schoolInfo = info;
        console.log(info);
        this.schoolInfo.picture = this._sanitizer.bypassSecurityTrustStyle(`url(${this.schoolInfo.pictureURL})`);
        this.schoolInfo.logo = this._sanitizer.bypassSecurityTrustStyle(`url(${this.schoolInfo.logoURL})`);


    }

    getDeadline(d) {
        let name = 'round' + d.toString() + "Deadline";
        return this.schoolInfo[name];
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