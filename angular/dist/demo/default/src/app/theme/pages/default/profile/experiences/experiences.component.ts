import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ProfileService } from '../../../../../_services/profile.service';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import { ScriptLoaderService } from '../../../../../_services/script-loader.service';

@Component({
    selector: 'app-experiences',
    templateUrl: './experiences.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class ExperiencesComponent implements OnInit {

    id = 1;
    @Input() profile:any;
    count:number = 4;
    questions = ["List some of your experiences that demonstrate your leadership capabilities.",
                "List some of your experiences that demonstrate you are a team player and you work well in a collaborative environment.",
                "List some of your experiences where you failed you achieve what you set out to. What did you learn from these failures?",
                "What are some of your accomplishments that you are most proud of, and why?"];
    questionNames = ["leadershipExperience", "teamPlayerExperience", "failureExperience", "accomplishments"];
    datas:any = [[],[],[],[]];
    types = [1,1,1,1];
    constructor(private _script: ScriptLoaderService) {

    }

    ngOnInit() {
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
        if (changes['profile'] && this.profile != undefined) {
            console.log(this.profile);
            for (var i = 0; i < this.count; i++) {
                this.datas[i] = this.profile[this.questionNames[i]];
            }
        
        }
    }
}