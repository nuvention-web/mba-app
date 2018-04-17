import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ProfileService } from '../../../../../_services/profile.service';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import { ProfileData } from '../profiledata'
@Component({
    selector: 'app-experiences',
    templateUrl: './experiences.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class ExperiencesComponent implements OnInit {

    id = 2;
    @Input() profile:any;
    count:number = 4;
    questions = ["List some of your experiences that demonstrate your leadership capabilities.",
                "List some of your experiences that demonstrate you are a team player and you work well in a collaborative environment.",
                "List some of your experiences where you failed you achieve what you set out to. What did you learn from these failures?",
                "What are some of your accomplishments that you are most proud of, and why?"];
    questionNames = ["leadershipExperience", "teamPlayerExperience", "failureExperience", "accomplishments"];
    datas:any[][] = [[], [], [], []];
    types = [1,1,1,1];
    isInit = true;
    constructor(private _profile: ProfileService) {
        console.log('on-constructor');
    }

    ngOnInit() {
        console.log('on-constructor');
    }
    ngAfterViewInit() {
        //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
        //Add 'implements AfterViewInit' to the class.
        this._script.loadScripts('app-portlets-tools',
            ['assets/demo/default/custom/components/portlets/tools.js']);
    }

    ngOnChanges(changes: SimpleChanges) {
        //Called before any other lifecycle hook. Use it to inject dependencies, but avoid any serious work here.
        //Add '${implements OnChanges}' to the class.
        if (this.isInit && changes['profile'] && this.profile != undefined) {
            for (let i = 0; i < this.count; i++) {
                this.datas[i] = ProfileData.wrap(this.profile[this.questionNames[i]]);
            }
            this.isInit = false;
        }
    }

    add(i) {
        if (i >= 0 && i < this.count) {
            this.datas[i].push(ProfileData.wrap(""));
        }
        this.save();
    }

    remove(i, j) {
        if (i >= 0 && i < this.count && j >=0 && j < this.datas[i].length) {
            this.datas[i].splice(j, 1);
        }
        this.save();
    }

    save() {
        for (let i = 0; i < this.count; i++) {
            this.profile[this.questionNames[i]] = ProfileData.unwrap(this.datas[i]);
        }
        this._profile.updateProfile(this.profile);
    }

}