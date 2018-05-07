import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { UsersService } from '../../../../_services/users.service';
import { ProfileService } from '../../../../_services/profile.service';
import { Location } from '@angular/common';

@Component({
    selector: "app-profile",
    templateUrl: "./profile.component.html",
    styleUrls: ['./profile.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class ProfileComponent implements OnInit, AfterViewInit {

    userInformation: any = {};
    id = 1;
    profile:any = undefined;
    token: string;
    constructor(private _script: ScriptLoaderService, private _user: UsersService, private _profile: ProfileService) {
        this._profile.getProfile(this.token).subscribe(p => this.profile = p);
    }
    ngOnInit() {
        
    }
    ngAfterViewInit() {
        
        // this._script.loadScripts('app-validation-form-controls',
        //     ['assets/demo/default/custom/components/forms/validation/form-controls.js']);
    }

}