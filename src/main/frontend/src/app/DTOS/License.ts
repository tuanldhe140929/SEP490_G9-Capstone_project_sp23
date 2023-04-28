export class License {
  id: number;
  name: string;
  acronyms:string;
  details: string;
  referenceLink: string;
  constructor() {
    this.id = -1;
    this.name = "";
    this.acronyms = "";
    this.details = "";
    this.referenceLink = "";
  }
}
