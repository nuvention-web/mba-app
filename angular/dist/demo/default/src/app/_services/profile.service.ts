import { Injectable } from '@angular/core';
import { Http, Response,Headers } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { URL, user} from './url-infos'

let header = new Headers({'Content-Type': 'application/json'});
let username = "john.doe@gmail.com";
let password = "hello";
let myURL = "https://myappmba-199623.appspot.com"
@Injectable()
export class ProfileService {

 constructor(private http: Http) { }

 authenticate() {
    return this.http.post(myURL + "/login", {"email": username, "password": password}, {headers: header});
 }

 setToken(token) {
     header.append('Authorization', token);
 }
 getProfile(token) {
    return this.http.get(URL+"/mba/users/"+user+"/profile",  {headers: new Headers({'Authorization': token})})
        .map((response: Response) => response.json());
 }

 updateProfile(profile) {
     this.http.put(URL+"/mba/users/" + user + "/profile", profile, {headers: header})
 }
}