import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import { URL, getCurrentUser, jwt} from './url-infos'

@Injectable()
export class SchoolsService {

    constructor(private http:Http) {
    }

    getSchools() {
        return this.http.get(URL + "/mba/users/" + getCurrentUser(), jwt()).map((response:Response) => response.json().schools);
    }

    getAllSchools() {
        return this.http.get(URL+"/mba" + "/schools", jwt()).map((response:Response) => response.json());
    }

    userDeleteSchool(schoolName:string) {
        this.http.delete(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName, jwt());
    }

    userAddSchool(schoolName:string) {
        return this.http.put(URL+"/mba/users/"+getCurrentUser()+"/school", {"schools": [schoolName]}
            , jwt(1));
    }

    getSchoolDetails(schoolName:string) {
        return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName, jwt()).map((response:Response) => response.json());
    }

    updateSchoolDetails(schoolName: string, schoolDetail) {
        return this.http.post(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName, schoolDetail, jwt())
    }

    getEssay(schoolName:string, essayID:string) {
        return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID, jwt()).map((response:Response) => response.json());
    }

    getEssayReviews(schoolName:string, essayID:string) {
        return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID + "/reviews", jwt()).map((response:Response) => response.json());
    }

    getDraft(schoolName:string, essayID:string, draftID:string) {
        return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID, jwt()).map((response:Response) => response.json());
    }

    runAnalysis(schoolName:string, essayID:string, draftID:string){
        return this.http.post(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID + "/scan/analysis",{}, jwt(1));
    }

    runProofRead(schoolName:string, essayID:string, draftID:string){
        return this.http.post(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID + "/scan/proofRead",{}, jwt(1));
    }

    getAllEssays() {
        return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/essays", jwt()).map((response:Response) => response.json());
    }

    deleteEssayDraft(schoolName:string, essayID:string, draftID:string) {
        return this.http.delete(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID, jwt());
    }

    downloadEssayDraft(schoolName:string, essayID:string, draftID:string) {
        window.open(URL+"/download/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID + "/draft/" + draftID);
    }

    getNote(schoolName:string, noteID:string) {
        return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/notes/" + noteID, jwt()).map((response:Response) => response.json());
    }

    getRecommender(schoolName:string, recommenderID:string) {
        return this.http.get(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/recommender/" + recommenderID, jwt()).map((response:Response) => response.json());
    }

    updateNote(schoolName:string, noteID:string, contents:string, title:string){
        return this.http.put(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/notes/" + noteID, {"contents": contents, "title": title}
            , jwt(1));
    }

    updateRecommender(schoolName:string, recommenderID:string, contents:string){
        return this.http.put(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/recommender/" + recommenderID, {"contents": contents}
            , jwt(1));
    }


    addNote(schoolName:string, contents:string, title:string){
        return this.http.post(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/notes/", {"contents": contents, "title": title}
            , jwt(1));
    }

    deleteNote(schoolName:string, noteID:string){
        return this.http.delete(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/notes/" + noteID, jwt(1));
    }

    uploadEssayDraft(file, schoolName:string, essayID:string) {
        let formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http.post(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID+"/upload/draft", formData, jwt(0));
    }

    saveEssayDraft(text, schoolName, essayID) {
        return this.http.post(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID + "/draft",{"contents": text}, jwt(1));
    }

    sendForReview(schoolName, essayID, draftID, email, name, message) {
        return this.http.post(URL+"/mba/users/"+getCurrentUser()+"/school/" + schoolName + "/essay/" + essayID+"/draft/"+draftID+"/email/", {
            "email": email,
            "name": name,
            "message": message
        }, jwt(1));
    }


}