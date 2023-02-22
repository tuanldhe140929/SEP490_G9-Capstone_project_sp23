

import { License } from "./License";
import { Preview } from "./Preview";
import { ProductFile } from "./ProductFile";
import { Seller } from "./Seller";
import { Category } from "./Category";
import { Tag } from "./Tag";

export class Product {
  id: number;
  version: string;
  name: string;
  description: string;
  coverImage: string;
  details: string;
  instruction: string;
  createdDate: Date
  lastModified: Date
  license: License;
  draft: boolean;
  price: number;
  previewVideo: Preview;
  previewPictures: Preview[];

  files: ProductFile[];
  seller: Seller;

  tags: Tag[];
  category: Category;

  constructor() {
    this.id = -1;
    this.version = "";
    this.name = "";
    this.description = "";
    this.price = 0;
    this.seller = new Seller();
    this.category = new Category();
    this.license = new License();
    this.files = [];
    this.tags = [];
    this.previewVideo = new Preview;
    this.previewPictures = [];
    this.coverImage = "";
    this.details = "";
    this.draft = true;
    this.instruction = "";
    this.createdDate = new Date();
    this.lastModified = new Date();

  }
}
