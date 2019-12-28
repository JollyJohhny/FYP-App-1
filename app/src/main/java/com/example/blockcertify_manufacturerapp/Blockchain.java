package com.example.blockcertify_manufacturerapp;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private int difficulty;
    public List<Block> blocks;

    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        blocks = new ArrayList<>();
//        // create the first block
//        Block b = new Block(0, System.currentTimeMillis(), null, "First Block");
//        b.mineBlock(difficulty);
//        blocks.add(b);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Block latestBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public Block newBlock(String data) {
        Block latestBlock = latestBlock();
        return new Block(latestBlock.getIndex() + 1, System.currentTimeMillis(),
                latestBlock.getHash(), data);
    }

    public void addBlock(Block b) {
        if (b != null) {
            b.mineBlock(difficulty);
            blocks.add(b);
        }
    }

    public boolean isFirstBlockValid() {
        Block firstBlock = blocks.get(0);

        if (firstBlock.getIndex() != 0) {
            return false;
        }

        if (firstBlock.getPreviousHash() != null) {
            return false;
        }

        return firstBlock.getHash() != null &&
                Block.calculateHash(firstBlock).equals(firstBlock.getHash());
    }

    public boolean isValidNewBlock(Block newBlock, Block previousBlock) {
        if (newBlock != null  &&  previousBlock != null) {
            if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
                return false;
            }

            if (newBlock.getPreviousHash() == null  ||
                    !newBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }

            return newBlock.getHash() != null &&
                    Block.calculateHash(newBlock).equals(newBlock.getHash());
        }

        return false;
    }

    public boolean isBlockChainValid() {
        if (!isFirstBlockValid()) {
            return false;
        }

        for (int i = 1; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            Block previousBlock = blocks.get(i - 1);

            if (!isValidNewBlock(currentBlock, previousBlock)) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Block block : blocks) {
            builder.append(block).append("\n");
        }

        return builder.toString();
    }

}