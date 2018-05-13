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
    schoolInfo: any = null;

    constructor(private route: ActivatedRoute, private _script: ScriptLoaderService, private _schools:SchoolsService, private _sanitizer: DomSanitizer) {
        this.route.params.subscribe( params =>
            this.school = params.school

        );
        this._schools.getSchoolDetails(this.school).subscribe(d => this.schoolDetails = d);
        this._schools.getSchoolInfos().subscribe(d => this.getSchoolInfo(d));
    }

    saveSchool(i) {
        console.log(this.schoolDetails.notes);
        this._schools.updateNote(this.school, this.schoolDetails.notes[i].noteID, this.schoolDetails.notes[i].contents, this.schoolDetails.notes[i].title);
    }

    getSchoolInfo(info) {
        var path:string;

        for (var i = 0; i < info.length; i++) {
            if (info[i].Shortname === this.school) {
                this.schoolInfo = info[i];
                path = schoolImageDir + this.schoolInfo.Shortname + '.jpg';
                this.schoolInfo.Picture = this._sanitizer.bypassSecurityTrustStyle(`url(${path})`);
                console.log(this.schoolInfo);
                return;
            }
        }
    }

    getDeadline(d) {
        let name = 'Deadline' + d.toString();
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