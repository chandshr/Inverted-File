# Inverted-File
Reads a collection of files from Cransfield collection and create and inverted file of the collection. The inverted file is stored in an external file(basically a big hash table) suitable for use by querying programs to be developed later.

Stop words are avoided
Terms are casefolded, and stemmed with teh porter stemmer
For any term in the hash, another hash table values are returned containing the document frequency (number of documents the term occurs in) each of which contains a document id and the term frequency (number of occurences of the term in the document)
























