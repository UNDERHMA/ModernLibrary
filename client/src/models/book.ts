export class Book{
    title: string;
    author: string;
    image: string;
    isbn: string;
    constructor(title: string, author:string, image: string, isbn: string){
        this.title = title;
        this.author = author;
        this.image = image;
        this.isbn = isbn;
    }
}