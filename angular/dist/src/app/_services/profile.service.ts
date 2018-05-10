import { Injectable } from '@angular/core';
import { Http, Response,Headers } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { URL, getCurrentUser, jwt} from './url-infos'

@Injectable()
export class ProfileService {

 constructor(private http: Http) { }

 getProfile(token) {
    return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/profile", jwt())
        .map((response: Response) => response.json());
 }

 updateProfile(profile) {
     this.http.put(URL+"/mba/users/" + getCurrentUser() + "/profile", profile, jwt(1));
 }
}