import sys
import os
import cPickle

def dumpToFile(obj, path):
    """
    Dump an object to a file specified by path
    """
    path = getRealPath(path)

    # Ensure directories exist
    d = os.path.dirname(path)
    if not os.path.exists(d):
        os.makedirs(d)

    f = open(path, 'w')
    try:
        cPickle.dump(obj, f)

    except Exception, e:
        raise e
    finally:
        f.close()

def loadFromFile(path):

    if not fileExists(path):
        return None

    f = open(getRealPath(path), 'r')
    try:
        return cPickle.load(f)
    except Exception, e:
        return None
    finally:
        f.close()

def fileExists(path):
    return os.path.exists(getRealPath(path))

def getRealPath(path):
    """
    Gets a file path relative to the directory that
    this script is being run from
    """

    return os.path.join(sys.path[0], path)