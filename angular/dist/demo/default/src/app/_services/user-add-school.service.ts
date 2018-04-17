import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { catchError } from 'rxjs/operators/catchError';
import { URL, user} from './url-infos'

@Injectable()
export class AddSchool {

    private url:string = URL+"/mba/users/"+user;
    constructor(private http: Http) {}

    handleError(msg, value) {
        console.log(msg + ' : ' + value);
    }
    addSchool(schoolName: string) {
        return this.http.post(this.url, schoolName);
    }  

}