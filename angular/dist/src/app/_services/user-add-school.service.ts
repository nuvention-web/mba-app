import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { URL, getCurrentUser} from './url-infos'

@Injectable()
export class AddSchool {

    private url:string = URL+"/mba/users/"+getCurrentUser();
    constructor(private http: Http) {}

    addSchool(schoolName: string) {
        return this.http.post(this.url, schoolName);
    }  

}