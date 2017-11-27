


**How to import all addresses from the block chain into the bloom filter**

The bloom filter have to be initialized with a proper size (a bit bigger that the number of inserts). Otherwise the bloom filter will not work and will return to much false positive results. 

Unfortunately it is currently not possible to pre-calculate the number of addresses within the block chain because of it's big size of about 160 GB (on Dec. 2017).
Therefore it is required to run the import twice or even more times in order to find the right size.

**TODO**
This could be improved when e.g. all addresses per blk*.dat will be stored into an intermediate text file that is used to rebuild the bloom filter.
A further advantage would be, that only the latest block file is needed to be imported.

 


 

