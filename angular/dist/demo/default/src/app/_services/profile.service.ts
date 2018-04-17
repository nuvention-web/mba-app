import { Injectable } from '@angular/core';
import { Http, Response,Headers } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { URL, user} from './url-infos'

let header = new Headers({'Content-Type': 'application/json'});
@Injectable()
export class ProfileService {

 constructor(private http: Http) { }

 getProfile() {
    return this.http.get(URL+"/mba/users/"+user+"/profile").map((response: Response) => response.json());
 }

 updateProfile(profile) {
     this.http.put(URL+"/mba/users/" + user + "/profile", profile, {headers: header})
 }
}