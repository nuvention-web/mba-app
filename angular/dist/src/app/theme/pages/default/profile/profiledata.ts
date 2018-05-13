


export class ProfileData {
    constructor (public content: string) {

    }

    static wrap(datas:any):any {
        if (datas == null) {
            return new ProfileData("");
        }
        if (typeof datas === "string") {
            return new ProfileData(datas)
        } else if (datas instanceof Array && datas.length > 0 && typeof datas[0] === "string") {
            let res = [];
            for (let i = 0; i < datas.length; i++) {
                res.push(new ProfileData(datas[i]));
            }
            return res;
        }
        return [''];
    }

    static unwrap(datas:any):any {
        if (datas instanceof ProfileData) {
            return datas.content;
        } else if (datas instanceof Array && datas[0] instanceof ProfileData) {
            let res = [];
            for (let i = 0; i < datas.length; i++) {
                res.push(datas[i].content);
            }
            return res;
        }
        return null;
    }
}