import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ProfileService } from '../../../../../_services/profile.service';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import { ScriptLoaderService } from '../../../../../_services/script-loader.service';

@Component({
    selector: 'app-interests',
    templateUrl: './interests.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class InterestsComponent implements OnInit {

    id = 2;
    @Input() profile:any;
    count:number = 2;
    questions = ["What are your hobbies / interests outside of work/school?",
                "What are you going to bring to the MBA program you are admitted to?",
                ];
    questionNames = ["hobbiesOrInterests", "whatDoYouBring"];
    datas:any = [[],[]];
    types = [1, 0];
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
            for(var i = 0; i < this.count; i++) {
                this.datas[i] = this.profile[this.questionNames[i]];
            }
        }
    }
}