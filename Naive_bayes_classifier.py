
import argparse, math, os, re, string, zipfile
from typing import DefaultDict, Generator, Hashable, Iterable, List, Sequence, Tuple
from collections import defaultdict
import numpy as np
from sklearn import metrics
from nltk import word_tokenize
from nltk.util import ngrams
from nltk.tokenize import RegexpTokenizer
import string

class Sentiment:
    """Naive Bayes model for predicting text sentiment"""

    def __init__(self, labels: Iterable[Hashable]):
        """Create a new sentiment model

        Args:
            labels (Iterable[Hashable]): Iterable of potential labels in sorted order.
        """
        self.positive_docs = 0
        self.negative_docs = 0
        self.positive_dict = {}
        self.negative_dict = {}
        self.total_pos = 0
        self.total_neg = 0
         

    def preprocess(self, example: str, id:str =None) -> List[str]:
        """Normalize the string into a list of words.

        Args:
            example (str): Text input to split and normalize
            id (str, optional): File name from training/test data (may not be available). Defaults to None.

        Returns:
            List[str]: Normalized words
        """
        
        
        return example.translate(str.maketrans("", "", string.punctuation)).lower().split()

        

    def add_example(self, example: str, label: Hashable, id:str = None):
        """Add a single training example with label to the model

        Args:
            example (str): Text input
            label (Hashable): Example label
            id (str, optional): File name from training/test data (may not be available). Defaults to None.
        """
        
        split_example = self.preprocess(example)
        
        if label == 1: #positive example
            self.positive_docs += 1
            for word in split_example:
                if word in self.positive_dict:
                    self.positive_dict[word] +=1
                else:
                    self.positive_dict[word] = 1
                self.total_pos += 1
        else: #negative exmaple 
            self.negative_docs += 1
            for word in split_example:
                if word in self.negative_dict:
                    self.negative_dict[word] += 1
                else:
                    self.negative_dict[word] = 1
                self.total_neg += 1
        
            
      
        
    def predict(self, example: str, pseudo=0.0001, id:str = None) -> Sequence[float]:
        """Predict the P(label|example) for example text, return probabilities as a sequence

        Args:
            example (str): Test input
            pseudo (float, optional): Pseudo-count for Laplace smoothing. Defaults to 0.0001.
            id (str, optional): File name from training/test data (may not be available). Defaults to None.

        Returns:
            Sequence[float]: Probabilities in order of originally provided labels
        """
        split_example = self.preprocess(example)
        
        num_pos_reviews = self.positive_docs
        num_neg_reviews = self.negative_docs
        total_reviews = num_pos_reviews + num_neg_reviews
        neg_review_prob = np.log(num_neg_reviews / total_reviews)
        pos_review_prob = np.log(num_pos_reviews / total_reviews)
        
        


        Np_w = self.total_pos
        Up_w = len(self.positive_dict)
        k = pseudo

        pos_den = Np_w + k * Up_w

        Nn_w = self.total_neg
        Un_w = len(self.negative_dict)

        neg_den = Nn_w + k * Un_w
        

        for word in split_example:
            if word in self.positive_dict:
                pos_word_prob = np.log((self.positive_dict[word] + k) / pos_den)
            
            else:
                pos_word_prob = np.log((0 + k) / pos_den)
            pos_review_prob += pos_word_prob
            if word in self.negative_dict:
                neg_word_prob = np.log((self.negative_dict[word] + k) / neg_den)    
            
            else:
                neg_word_prob = np.log((0 + k) / neg_den)
            neg_review_prob += neg_word_prob  

        

        main_den = np.logaddexp.reduce([pos_review_prob, neg_review_prob])

        final_pos_prob = np.exp(pos_review_prob - main_den)

        final_neg_prob = np.exp(neg_review_prob - main_den)







        return [final_neg_prob, final_pos_prob]

class CustomSentiment(Sentiment):
    
    def __init__(self, labels: Iterable[Hashable]):
        super().__init__(labels)
        self.positive_docs = 0
        self.negative_docs = 0
        self.positive_dict = {}
        self.negative_dict = {}
        self.total_pos = 0
        self.total_neg = 0
    
    def preprocess(self, example: str, id:str =None, n_gram:int = 2) -> List[str]:
        """Normalize the string into a list of words.

        Args:
            example (str): Text input to split and normalize
            id (str, optional): File name from training/test data (may not be available). Defaults to None.
            n_gram: n-character subsets of the words. Defaults to 2 for bigrams

        Returns:
            List[str]: Normalized words
        """
       

        
        unigram = example.translate(str.maketrans("", "", string.punctuation)).lower().split()
        

        token = RegexpTokenizer(r'\w+')
        strs = token.tokenize(example)
        bigram = list(ngrams(strs,n_gram))
        
        for x in unigram:
            bigram.append(x)

        return bigram 

    def add_example(self, example: str, label: Hashable, id:str = None):
        """Add a single training example with label to the model

        Args:
            example (str): Text input
            label (Hashable): Example label
            id (str, optional): File name from training/test data (may not be available). Defaults to None.
        """
        
        split_example = self.preprocess(example)
        
        if label == 1: #positive example
            self.positive_docs += 1
            for word in split_example:
                if word in self.positive_dict:
                    self.positive_dict[word] +=1
                else:
                    self.positive_dict[word] = 1
                self.total_pos += 1
        else: #negative exmaple 
            self.negative_docs += 1
            for word in split_example:
                if word in self.negative_dict:
                    self.negative_dict[word] += 1
                else:
                    self.negative_dict[word] = 1
                self.total_neg += 1

    def predict(self, example: str, pseudo=0.0001, id:str = None) -> Sequence[float]:
        """Predict the P(label|example) for example text, return probabilities as a sequence

        Args:
            example (str): Test input
            pseudo (float, optional): Pseudo-count for Laplace smoothing. Defaults to 0.0001.
            id (str, optional): File name from training/test data (may not be available). Defaults to None.

        Returns:
            Sequence[float]: Probabilities in order of originally provided labels
        """
        split_example = self.preprocess(example)
        
        num_pos_reviews = self.positive_docs
        num_neg_reviews = self.negative_docs
        total_reviews = num_pos_reviews + num_neg_reviews
        neg_review_prob = np.log(num_neg_reviews / total_reviews)
        pos_review_prob = np.log(num_pos_reviews / total_reviews)
        
        


        Np_w = self.total_pos
        Up_w = len(self.positive_dict)
        k = pseudo

        pos_den = Np_w + k * Up_w

        Nn_w = self.total_neg
        Un_w = len(self.negative_dict)

        neg_den = Nn_w + k * Un_w
        

        for word in split_example:
            if word in self.positive_dict:
                pos_word_prob = np.log((self.positive_dict[word] + k) / pos_den)
            
            else:
                pos_word_prob = np.log((0 + k) / pos_den)
            pos_review_prob += pos_word_prob
            if word in self.negative_dict:
                neg_word_prob = np.log((self.negative_dict[word] + k) / neg_den)    
            
            else:
                neg_word_prob = np.log((0 + k) / neg_den)
            neg_review_prob += neg_word_prob  

       
        main_den = np.logaddexp.reduce([pos_review_prob, neg_review_prob])

        final_pos_prob = np.exp(pos_review_prob - main_den)

        final_neg_prob = np.exp(neg_review_prob - main_den)







        return [final_neg_prob, final_pos_prob]



def process_zipfile(filename: str) -> Generator[Tuple[str, str, int], None, None]:
    """Create generator of labeled examples from a Zip file that yields a tuple with
    the id (filename of input), text snippet and label (0 or 1 for negative and positive respectively).

    You can use the generator as a loop sequence, e.g.

    for id, example, label in process_zipfile("test.zip"):
        # Do something with example and label

    Args:
        filename (str): Name of zip file to extract examples from

    Yields:
        Generator[Tuple[str, str, int], None, None]: Tuple of (id, example, label)
    """
    with zipfile.ZipFile(filename) as zip:
        for info in zip.infolist():
            # Iterate through all file entries in the zip file, picking out just those with specific ratings
            match = re.fullmatch(r"[^-]+-(\d)-\d+.txt", os.path.basename(info.filename))
            if not match or (match[1] != "1" and match[1] != "5"):
                # Ignore all but 1 or 5 ratings
                continue
            # Extract just the relevant file the Zip archive and yield a tuple
            with zip.open(info.filename) as file:
                yield (
                    match[0],
                    file.read().decode("utf-8", "ignore"),
                    1 if match[1] == "5" else 0,
                )


def compute_metrics(y_true, y_pred):
    """Compute metrics to evaluate binary classification accuracy

    Args:
        y_true: Array-like ground truth (correct) target values.
        y_pred: Array-like estimated targets as returned by a classifier.

    Returns:
        dict: Dictionary of metrics in including confusion matrix, accuracy, recall, precision and F1
    """
    return {
        "confusion": metrics.confusion_matrix(y_true, y_pred),
        "accuracy": metrics.accuracy_score(y_true, y_pred),
        "recall": metrics.recall_score(y_true, y_pred),
        "precision": metrics.precision_score(y_true, y_pred),
        "f1": metrics.f1_score(y_true, y_pred),
    }


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Train Naive Bayes sentiment analyzer")

    parser.add_argument(
        "--train",
        default="data/train.zip",
        help="Path to zip file or directory containing training files.",
    )
    parser.add_argument(
        "--test",
        default="data/test.zip",
        help="Path to zip file or directory containing testing files.",
    )
    parser.add_argument(
        "-m", "--model", default="base", help="Model to use: One of base or custom"
    )
    parser.add_argument("example", nargs="?", default=None)

    args = parser.parse_args()

    # Train model
    if args.model == "custom":
        model = CustomSentiment(labels=[0, 1])
    else:
        model = Sentiment(labels=[0, 1])
    for id, example, y_true in process_zipfile(
        os.path.join(os.path.dirname(__file__), args.train)
    ):
        model.add_example(example, y_true, id=id)

    # If interactive example provided, compute sentiment for that example
    if args.example:
        print(model.predict(args.example))
    else:
        predictions = []
        for id, example, y_true in process_zipfile(
            os.path.join(os.path.dirname(__file__), args.test)
        ):
            # Determine the most likely class from predicted probabilities
            predictions.append((id, y_true, np.argmax(model.predict(example,id=id))))

        # Compute and print accuracy metrics
        _, y_test, y_true = zip(*predictions)
        predict_metrics = compute_metrics(y_test, y_true)
        for met, val in predict_metrics.items():
            print(
                f"{met.capitalize()}: ",
                ("\n" if isinstance(val, np.ndarray) else ""),
                val,
                sep="",
            )

